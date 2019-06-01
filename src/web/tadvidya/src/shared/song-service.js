import Configuration from './configuration'

class SongService {
    constructor() {
        this.config = new Configuration()
    }

    async retrieveSongs() {

        return fetch(this.config.SONG_COLLECTION_URL, {
            method: 'GET'
        })
            .then(response => {
                console.log(response.status);
                if (!response.ok) {
                    this.handleResponseError(response);
                }
                return response.json();
            })
            .then(json => {
                console.log("Retrieved songs:");
                console.log(json);
                const songs = [];
                const songArray = json;
                for(let i = 0; i < songArray.length; i++) {
                    songs.push(songArray[i]);
                }
                return songs;
            })
            .catch(error => {
                this.handleError(error);
            });
    }

    async searchForSongs(searchString) {
        console.log(this.config.SONG_SEARCH_URL+searchString)
        return fetch(this.config.SONG_SEARCH_URL+searchString, {
            method: 'GET'
        })
            .then(response => {
                console.log(response.status);
                if (!response.ok) {
                    this.handleResponseError(response);
                }
                return response.json();
            })
            .then(json => {
                console.log("Retrieved songs:");
                console.log(json);
                const songs = [];
                const songArray = json;
                for(let i = 0; i < songArray.length; i++) {
                    songs.push(songArray[i]);
                }
                return songs;
            })
            .catch(error => {
                this.handleError(error);
            });

    }

    async getSong(id) {
        for (var i = 0; i < this.songs.length; i++) {
            if (this.songs[i].id === id) {
                return Promise.resolve(this.songs[i])
            }
        }
        return null
    }

    handleResponseError(response) {
        throw new Error("HTTP error, status = " + response.status);
    }
    handleError(error) {
        console.log(error.message);
    }

}

export default SongService



