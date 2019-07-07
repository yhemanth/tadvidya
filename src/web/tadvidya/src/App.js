import React, { Component } from 'react';
import './App.css';
import SongService from './shared/song-service';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

class App extends Component {

  render() {
    return (
        <Router>
          <div>
            <Route exact path="/" component={Home} />
            <Route path="/songs/:songId" component={SongDetails} />
          </div>
        </Router>
    );
  }

}

class Home extends Component {

    constructor(props) {
        super(props);
        this.songService = new SongService();
        this.onSelect = this.onSelect.bind(this);
        this.onSearch = this.onSearch.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);

        this.state = {
            showDetails: false,
            selectedSong: null,
            searchString: ''
        }
    }

    componentDidMount() {
        this.getSongs();
    }

    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }

    getSongs() {
        this.songService.retrieveSongs().then(songs => {
                this.setState({songs: songs});
            }
        );
    }

    onSearch() {
        console.log(this.state.searchString)
        this.songService.searchForSongs(this.state.searchString).then(songs => {
            this.setState({songs: songs})
        })
    }

    onSelect(id) {
        this.clearState();
        this.songService.getSong(id).then(song => {
                this.setState({
                    showDetails: true,
                    selectedSong: song
                });
            }
        );
    }

    clearState() {
        this.setState({
            showDetails: false,
            selectedSong: null,
            editItem: false,
            newItem: null
        });
    }

    render() {
        const songs = this.state.songs;
        if (!songs)
            return null;

        const listSongs = songs.map((song) =>
            <tr key={song.id}>
                <td>
                    <a href={'/songs/'+song.id}>{song.title}</a>
                </td>
                <td>{song.raagam}</td>
                <td>{song.composer}</td>
            </tr>
        );

        return (
            <div>
                <div className="header">
                    <img src="tv6464.png" alt="Tadvidya" />
                    <h1>Tadvidya - For Connoisseurs of Carnatic Music</h1>
                    <hr/>
                </div>
                <div className="search-panel">
                    <label className="search-string">Find Songs:
                        <input value={this.state.searchString} size="100" maxLength="100" name="searchString" required
                               onChange={this.handleInputChange} placeholder="song title" />
                    </label>
                    <button onClick={() => this.onSearch()}>Go</button>
                </div>

                <br/>

                <table>
                    <tbody>
                    <tr>
                        <th>Song Title</th>
                        <th>Raagam</th>
                        <th>Composer</th>
                    </tr>
                    {listSongs}
                    </tbody>
                </table>
            </div>
        );
    }
}

class SongDetails extends Component {

    constructor(props) {
        super(props);
        this.songService = new SongService();
        this.state = {}
    }

    componentDidMount() {
        this.loadSong(this.props.match.params.songId);
    }

    loadSong(songId) {
        this.songService.getSong(songId).then(sd => {
            this.setState ({
                songDetails: sd
            });
        })
    }


    render() {
        const sd = this.state.songDetails;
        if (!sd)
            return null;

        return (
            <div>
                <div className="header">
                    <img src="../../tv6464.png" alt="Tadvidya" />
                    <h1>Tadvidya - For Connoisseurs of Carnatic Music</h1>
                </div>
                <div>
                    <Link to="/">Home</Link>
                </div>
                <hr/>
                <h2>
                    Song Title: {sd.title}
                </h2>
                <table>
                    <tbody>
                    <tr>
                        <td>Raagam</td>
                        <td>{sd.raagam}</td>
                    </tr>
                    <tr>
                        <td>Taalam</td>
                        <td>{sd.taalam}</td>
                    </tr>
                    <tr>
                        <td>Song Composer</td>
                        <td>{sd.composer}</td>
                    </tr>
                    <tr>
                        <td>Language</td>
                        <td>{sd.language}</td>
                    </tr>
                    </tbody>
                </table>
                <h3>
                    Lyrics:
                </h3>
                <div>
                    {this.breakIntoLines(sd.lyrics)}
                </div>
            </div>
        );
    }

    breakIntoLines(lyrics) {
        return lyrics.split('+').map((line) =>
            <p>{line}<br/></p>
        )
    }

}

export default App;
