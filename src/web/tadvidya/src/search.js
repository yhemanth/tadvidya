import React, { Component } from 'react';
import './App.css';

class Search extends Component {

    constructor(props) {
        super(props);
        this.onSubmit = this.onSubmit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.state = {
            searchString: ""
        };
    }

    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }

    onSubmit() {
        console.log(this.state.searchString)
    }

    render() {
        return (
            <div className="search-panel">
                <label className="search-string">Find Songs:
                    <input value={this.state.searchString} size="100" maxLength="100" name="searchString" required onChange={this.handleInputChange} placeholder="song title" />
                </label>
                <button onClick={() => this.onSubmit()}>Go</button>
            </div>
        )
    }

}

export default Search;
