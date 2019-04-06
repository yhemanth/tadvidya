from bs4 import BeautifulSoup
import collections
import collections.abc
import json
import logging
import os

class Song:

    def __init__(self, title, composer='', language=''):
        self.title = title
        self.composer = composer
        self.language = language

    def __str__(self):
        return str(json.dumps(self.__dict__))

class PipelineStepContext:
    def __init__(self, context_id, value):
        self.id = context_id
        self.item = value

class PipelineStep:
    def execute(self, context):
        pass


class NullPipelineStepContext(PipelineStepContext):
    def __init__(self, context_id):
        PipelineStepContext.__init__(self, context_id, None)


class SongDetailExtractionStep(PipelineStep):
    def execute(self, context):
        karnatik_song_raw_content_html = context.item
        song_detail_extractor = BeautifulSoup(karnatik_song_raw_content_html, 'html.parser')
        song_attribute_text = None
        for content in list(song_detail_extractor.children):
            if "raagam" in str(content):
                song_attribute_text = str(content)
                logging.debug(song_attribute_text)
        if not song_attribute_text:
            logging.error("Did not find attribute 'raagam' in text - unable to parse song content for ID {}".format(context.id))
            return NullPipelineStepContext(context.id)
        return PipelineStepContext(context.id, song_attribute_text)

class SongAttributeLines:
    def __init__(self, song_attribute_lines):
        self.song_attribute_lines = song_attribute_lines

    def __str__(self):
        return str(self.song_attribute_lines)

    def __iter__(self):
        return SongAttributesIterator(self.song_attribute_lines)

    def song_title(self):
        return self.song_attribute_lines[0].strip()

class SongAttributesIterator(collections.abc.Iterator):
    def __init__(self, song_attribute_lines):
        self.song_attribute_lines = song_attribute_lines
        self.current_index = 1 # First line is title of song.
        self.known_attributes = ['language', 'composer']

    def __next__(self):
        while self.current_index < len(self.song_attribute_lines):
            maybe_attribute = self.song_attribute_lines[self.current_index]
            self.current_index += 1
            attribute_parts = maybe_attribute.split(':')
            attribute_name = attribute_parts[0].lower()
            if attribute_name and attribute_name in self.known_attributes:
                logging.debug("found attribute_name {} and value {}".format(attribute_name, attribute_parts[1].strip()))
                return attribute_name, attribute_parts[1].strip()
        raise StopIteration


class SongAttributeLinesBuilderStep(PipelineStep):
    def execute(self, context):
        song_attribute_lines = context.item
        song_attribute_line_builder = BeautifulSoup(song_attribute_lines, 'html.parser')
        song_attribute_lines = song_attribute_line_builder.text.split('\n')
        song_attribute_lines = SongAttributeLines(self.select_non_empty_lines(song_attribute_lines))
        logging.debug(song_attribute_lines)
        return PipelineStepContext(context.id, song_attribute_lines)

    @staticmethod
    def select_non_empty_lines(song_attribute_lines):
        return [sd for sd in song_attribute_lines if sd.strip()]

class SongBuilderStep(PipelineStep):
    def execute(self, context):
        song_attribute_lines = context.item
        song_attributes = {}
        for attribute_name, attribute_value in song_attribute_lines:
            song_attributes[attribute_name] = attribute_value
        song = Song(song_attribute_lines.song_title(), **song_attributes)
        logging.info("Song: {}".format(song))


class SongTransformationPipeline:
    def __init__(self, song_id, raw_song_details):
        self.song_id = song_id
        self.init_value = raw_song_details
        self.steps = [SongDetailExtractionStep(), SongAttributeLinesBuilderStep(), SongBuilderStep()]

    def execute_pipeline(self):
        context = PipelineStepContext(self.song_id, self.init_value)
        for step in self.steps:
            context = step.execute(context)
            if isinstance(context, NullPipelineStepContext):
                logging.error("Found NullPipelineStepContext in step {} for song ID {}. Will halt pipeline".format(step, self.song_id))
                return

def read_file(file_name):
    with open(file_name) as in_fp:
        lines = in_fp.readlines()
    return ''.join(lines)

if __name__ == '__main__':
    logging.basicConfig(level=logging.INFO)
    song_files_dir = '/Users/hemanth/projects/personal/tadvidya/tadvidya/data/'
    for file in os.listdir(song_files_dir):
        _raw_song_details = read_file(os.path.join(song_files_dir, file))
        pipeline = SongTransformationPipeline(file, _raw_song_details)
        pipeline.execute_pipeline()
