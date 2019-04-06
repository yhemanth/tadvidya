
import requests
import time

SEED_LIST = '/Users/hemanth/projects/personal/tadvidya/tadvidya/song_list_sample_urls.txt'
DATA_DIR = '/Users/hemanth/projects/personal/tadvidya/tadvidya/data/'

class HTMLClient:
    @staticmethod
    def get_url(url):
        song_details = requests.get(url, verify=False)
        return song_details.text

class SongDBCrawler:
    def __init__(self, seed_list, data_dir):
        self.seed_list = seed_list
        self.data_dir = data_dir

    def start_crawl(self):
        with open(self.seed_list) as in_fp:
            url = in_fp.readline()
            while url:
                stripped_url = url.strip()
                song_id = stripped_url.split(sep='/')[-1]
                song_file_name = '{}{}.txt'.format(self.data_dir, song_id)
                with open(song_file_name, mode='w') as out_fp:
                    print('Fetching URL {} for song ID {} and saving to file {}'.format(stripped_url, song_id,
                                                                                        song_file_name))
                    out_fp.writelines(HTMLClient.get_url(stripped_url))
                time.sleep(15)
                url = in_fp.readline()

if __name__ == '__main__':
    song_db_crawler = SongDBCrawler(SEED_LIST, DATA_DIR)
    song_db_crawler.start_crawl()

