class SongService {
    constructor() {
        this.songSummaries = [
            {"id":843,"title":"Rama Ni Samaanamevaru","composer":"Tyaagaraaja","language":"Telugu"},
            {"id":981,"title":"Paahi Rama Rama","composer":"Tyaagaraaja","language":"Telugu"},
            {"id":988,"title":"Raamaa Raamaa Ramachandra","composer":"Tyaagaraaja","language":"Telugu"}
        ]
        this.songs = [
            {"id":843,"title":"Rama Ni Samaanamevaru","composer":"Tyaagaraaja","language":"Telugu",
                "raagam":"kharaharapriyaa","taalam":"roopakam",
                "lyrics":"pallavi+pallavi+ rAma nI samAnamevaru raghu vamshOddhAraka (samAnamevaru, nI samAnamevaru)+anupallavi+ bhAma maruvampu molaka bhaktiyanu panjarapu ciluka+caraNam+ paluka palukulaku tEnelOluku mATalADu sOdarugala+hari tyAgarAja kula vibhUSaNa mrdu bhASaNa"},
            {"id":981,"title":"Paahi Rama Rama","composer":"Tyaagaraaja","language":"Telugu",
                "raagam":"kharaharapriyaa","taalam":"tishra laghu eka",
                "lyrics":"pallavi+ pAhi rAma rAma anucu bhajana sEyavE+caraNam 1+ kanikarambu galgi sIta kAntuni kanagA manasu ranjilla balke madana janakuDu+caraNam 2+ vallavu diddi saumitri valaci nilvaga kaluva rEkulanu gEru kanula jUcenu+caraNam 3+ bharatu dAvEla karagi karagi nilvAga karamu baTTi kaugalincE varaduDappuDu+caraNam 4+ caNDa shatrughna dappuDu khaNDa vruttitO nuNDa santasillE kOdaNDa rAmuDu+caraNam 5+ manasu delisi kalisi hanumantuDuNDaga canavu mATaluDu cuNTe sArva bhaumuDu+caraNam 6+ vIri karuNa kaligi epuDu vElasi uNDunO sAramaina bhaktice sannutintunO+caraNam 7+ dharmArtha kAma mOkSa dAnamElanE marma mErugalEni indra sharma mElanE+caraNam 8+ bAga karuNa jEsi epuDu bhavya mosagunO tyAgarAju ceyi baTTi dayanu brOcunO"},
            {"id":988,"title":"Raamaa Raamaa Ramachandra","composer":"Tyaagaraaja","language":"Telugu",
                "raagam":"ghanta","taalam":"jhampa",
                "lyrics":"pallavi+ rAma rAma rAmacandra shrI rAma rAma guna sAndra+caraNam 1+ ghallu ghallu nI karamu baTTi krIgannula gaNTE munnadi taramu+caraNam 2+ nA jApu nI jUpu sarigA jEsitE jEyu nA sukhamu evariki eruga+caraNam 3+ idi buddhi anucu Anatiyya vErE vElpu lETiki danaku rAmayya+caraNam 4+ ceTTa baTTini nA bhIti rAma boTTu gaTTani kanyaka rIti+caraNam 5+ evarIki dagunE bAga rAju puvvalE rAma tyAgarAa paripAla+Other information:+Lyrics contributed by Lakshman Ragde. This song shows nayaka-nayaki bhava as described here."}
        ]

    }

    async retrieveSongs() {
        return Promise.resolve(this.songSummaries)
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