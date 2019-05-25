class SongService {
    constructor() {
        this.songs = [
            {"id":45,"title":"Rama Ni Samaanamevaru","composer":"Tyaagaraaja","language":"Telugu"},
            {"id":183,"title":"Paahi Rama Rama","composer":"Tyaagaraaja","language":"Telugu"},
            {"id":190,"title":"Raamaa Raamaa Ramachandra","composer":"Tyaagaraaja","language":"Telugu"}
        ]
    }

    async retrieveSongs() {
        return Promise.resolve(this.songs)
    }

    async getSong(id) {
        for (var i = 0; i < this.songs.length; i++) {
            if (this.songs[i].id === id) {
                return Promise.resolve(this.songs[i])
            }
        }
        return null
    }
}

export default SongService