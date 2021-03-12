import React, { Component } from 'react';

class URLForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            submissionSuccess: null,
            URLShort: '',
            URLPref: ''};

        this.handleURLChange = this.handleURLChange.bind(this);
        this.handlePrefChange = this.handlePrefChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleURLChange(event) {
        this.setState({URLShort: event.target.value});
    }

    handlePrefChange(event) {
        this.setState({URLPref: event.target.value});
    }

    handleSubmit(event) {
        // Send inputs to API
    }

    render(){
        return(
            <form onSubmit={this.handleSubmit}>
            <label>
              URL to Shorten:
              <input type="text" value={this.state.URLShort} onChange={this.handleURLChange} />
            </label>
            <label>
              Preferred host URL:
              <input type="text" value={this.state.URLPref} onChange={this.handlePrefChange} />
            </label>
            <input type="submit" value="Submit" />
          </form>
        )
    }
}

export default URLForm;