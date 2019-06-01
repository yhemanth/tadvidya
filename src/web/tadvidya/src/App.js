import React, { Component } from 'react';
import './App.css';
import SongService from './shared/song-service';

class App extends Component {

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

  render() {
    const songs = this.state.songs;
    if (!songs)
      return null;

    const listSongs = songs.map((song) =>
        <tr key={song.id}>
          <td>
            <a href={'http://localhost:9000/v1/songs/'+song.id}>{song.title}</a>
          </td>
          <td>{song.composer}</td>
          <td>{song.language}</td>
        </tr>
    );

    return (
        <div>

          <div className="search-panel">
            <label className="search-string">Find Songs:
              <input value={this.state.searchString} size="100" maxLength="100" name="searchString" required onChange={this.handleInputChange} placeholder="song title" />
            </label>
            <button onClick={() => this.onSearch()}>Go</button>
          </div>

          <br/>

          <table>
            <tbody>
              <tr>
                <th>Song Title</th>
                <th>Composer</th>
                <th>Language</th>
              </tr>
              {listSongs}
            </tbody>
          </table>
        </div>
    );
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
}
export default App;
