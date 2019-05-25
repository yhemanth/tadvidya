import React, { Component } from 'react';
import './App.css';
// import ItemDetails from './item-details';
// import NewItem from './new-item';
// import EditItem from './edit-item';
import SongService from './shared/song-service';

class App extends Component {

  constructor(props) {
    super(props);
    this.songService = new SongService();
    this.onSelect = this.onSelect.bind(this);
    // this.onNewItem = this.onNewItem.bind(this);
    // this.onEditItem = this.onEditItem.bind(this);
    // this.onCancel = this.onCancel.bind(this);
    // this.onCancelEdit = this.onCancelEdit.bind(this);
    // this.onCreateItem = this.onCreateItem.bind(this);
    // this.onUpdateItem = this.onUpdateItem.bind(this);
    // this.onDeleteItem = this.onDeleteItem.bind(this);
    this.state = {
      showDetails: false,
      // editItem: false,
      selectedSong: null,
      // newItem: null
    }
  }

  componentDidMount() {
    this.getSongs();
  }

  render() {
    const songs = this.state.songs;
    if(!songs) return null;
    // const showDetails = this.state.showDetails;
    // const selectedSong = this.state.selectedSong;
    // const newItem = this.state.newItem;
    // const editItem = this.state.editItem;

    const listSongs = songs.map((song) =>
        <tr key={song.id} onClick={() => this.onSelect(song.id)}>
          <td>{song.title}</td>
          <td>{song.composer}</td>
          <td>{song.language}</td>
        </tr>
    );

    return (
        <table>
          <tr>
            <th>Song Title</th>
            <th>Composer</th>
            <th>Language</th>
          </tr>

          {listSongs}
          {/*<br/>*/}
          {/*<button type="button" name="button" onClick={() => this.onNewItem()}>New Item</button>*/}
          {/*<br/>*/}
          {/*{newItem && <NewItem onSubmit={this.onCreateItem} onCancel={this.onCancel}/>}*/}
          {/*{showDetails && selectedSong && <ItemDetails item={selectedSong} onEdit={this.onEditItem}  onDelete={this.onDeleteItem} />}*/}
          {/*{editItem && selectedSong && <EditItem onSubmit={this.onUpdateItem} onCancel={this.onCancelEdit} item={selectedSong} />}*/}
        </table>
    );
  }

  getSongs() {
    this.songService.retrieveSongs().then(songs => {
          this.setState({songs: songs});
        }
    );
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
  // onCancel() {
  //   this.clearState();
  // }
  // onNewItem() {
  //   this.clearState();
  //   this.setState({
  //     newItem: true
  //   });
  // }
  // onEditItem() {
  //   this.setState({
  //     showDetails: false,
  //     editItem: true,
  //     newItem: null
  //   });
  // }
  // onCancelEdit() {
  //   this.setState({
  //     showDetails: true,
  //     editItem: false,
  //     newItem: null
  //   });
  // }
  // onUpdateItem(item) {
  //   this.clearState();
  //   this.songService.updateItem(item).then(item => {
  //         this.getSongs();
  //       }
  //   );
  // }
  // onCreateItem(newItem) {
  //   this.clearState();
  //   this.songService.createItem(newItem).then(item => {
  //         this.getSongs();
  //       }
  //   );
  // }
  // onDeleteItem(itemLink) {
  //   this.clearState();
  //   this.songService.deleteItem(itemLink).then(res => {
  //         this.getSongs();
  //       }
  //   );
  // }
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
