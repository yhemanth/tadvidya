from bs4 import BeautifulSoup
import collections
import collections.abc
import json
import logging
import os
import requests
import sys

class Song:

    def __init__(self, title, composer='', language='', raagam='', taalam='', lyrics=''):
        self.title = title.title()
        self.composer = composer.title() if composer else "Unknown"
        self.language = language.capitalize() if language else "Unknown"
        self.raagam = raagam
        self.taalam = taalam
        self.lyrics = lyrics

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
        self.current_index = 1 # Index 0 is title of song.
        self.song_metadata_attributes = ['raagam', 'taalam', 'composer', 'language']
        self.found_all_metadata_attributes = False
        self.extracted_lyrics = False

    def __next__(self):
        while self.current_index < len(self.song_attribute_lines):
            maybe_attribute = self.song_attribute_lines[self.current_index].strip().lower()
            self.current_index += 1
            attribute_parts = maybe_attribute.split(':')
            attribute_name = attribute_parts[0].lower()
            if attribute_name and attribute_name in self.song_metadata_attributes:
                logging.debug("found attribute_name {} and value {}".format(attribute_name, attribute_parts[1].strip()))
                if attribute_name == 'language':
                    self.found_all_metadata_attributes = True
                return attribute_name, attribute_parts[1].strip()
            elif (self.__maybe_start_of_lyrics(maybe_attribute)) and not self.extracted_lyrics:
                return self.__extract_lyrics(maybe_attribute)
        raise StopIteration

    def __maybe_start_of_lyrics(self, maybe_attribute):
        return maybe_attribute.startswith('pallavi') or self.found_all_metadata_attributes

    def __extract_lyrics(self, first_line_of_lyrics):
        lyrics = [first_line_of_lyrics]
        while self.current_index < len(self.song_attribute_lines) and not self.__end_of_lyrics():
            lyrics.append(self.song_attribute_lines[self.current_index])
            self.current_index += 1
        self.extracted_lyrics = True
        lyrics_str = '+'.join(lyrics)
        logging.debug('lyrics: ' + str(lyrics_str))
        return 'lyrics', lyrics_str

    def __end_of_lyrics(self):
        line = self.song_attribute_lines[self.current_index]
        is_end = line.lower().startswith('meaning') or line.lower().startswith('first')
        return is_end


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
        return PipelineStepContext(context.id, song)

class SongAdderStep(PipelineStep):

    def __init__(self, tadvidya_api_server):
        self.tadvidya_api_url = 'http://%s/v1/songs' % tadvidya_api_server

    def execute(self, context):
        response = requests.post(self.tadvidya_api_url, json=json.loads(str(context.item)))
        logging.info("Posted content and got response: {}".format(response.status_code))


class SongTransformationPipeline:
    def __init__(self, song_id, raw_song_details, tadvidya_api_server):
        self.song_id = song_id
        self.init_value = raw_song_details
        self.steps = [SongDetailExtractionStep(), SongAttributeLinesBuilderStep(), SongBuilderStep(), SongAdderStep(tadvidya_api_server)]

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
    song_files_dir = '/Users/hemanth/projects/personal/tadvidya/tadvidya/data'
    if len(sys.argv) > 1:
        song_files_dir = sys.argv[1]
    tadvidya_api_server = 'localhost:9000'
    if len(sys.argv) > 2:
        tadvidya_api_server = sys.argv[2]
    # song_files_dir = '/Users/hemanth/temp/songs'
    for file in os.listdir(song_files_dir):
        _raw_song_details = read_file(os.path.join(song_files_dir, file))
        pipeline = SongTransformationPipeline(file, _raw_song_details, tadvidya_api_server)
        pipeline.execute_pipeline()
