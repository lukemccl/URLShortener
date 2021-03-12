import React, { Component } from 'react';

class URLForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            submissionSuccess: null,
            submittedURL: '',
            URLShort: '',
            URLPref: ''};

        this.handleURLChange = this.handleURLChange.bind(this);
        this.handlePrefChange = this.handlePrefChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleReset = this.handleReset.bind(this);
    }

    //separate handlers to keep track of both boxes
    handleURLChange(event) {
        this.setState({URLShort: event.target.value});
    }

    handlePrefChange(event) {
        this.setState({URLPref: event.target.value});
    }

    handleSubmit(event) {
        // Send inputs to API
        const newState = this.state; 

        if(this.state.URLShort == '') return //block empty URLs -- need to change to show error somehow

        newState.submissionSuccess = true;
        newState.submittedURL = newState.URLPref;
        this.setState(newState);
    }

    handleReset(event) {
        // Reset state for new shorten
        this.setState({
            submissionSuccess: null,
            submittedURL: '',
            URLShort: '',
            URLPref: ''});
    }

    render(){
        const submissionSuccess = this.state.submissionSuccess;
        const submittedURL = this.state.submittedURL ? this.state.submittedURL : 'random String';
        let submitBox;
        if(!submissionSuccess){
            submitBox = 
            <form onSubmit={this.handleSubmit}>
                <div>
                    <label>
                        URL to Shorten:
                        <input type="text" value={this.state.URLShort} onChange={this.handleURLChange} />
                    </label>
                </div>
                <div>
                    <label>
                        Preferred host URL:
                        <input type="text" value={this.state.URLPref} onChange={this.handlePrefChange} />
                    </label>
                </div>
                <input type="submit" value="Submit" />
            </form>
        }else{
            submitBox = 
            <div>
                Url successfully shortened to <a href={submittedURL}>{submittedURL}</a>
                <button onClick={this.handleReset}>
                Shorten another URL!
                </button>
            </div>
        }

        return(submitBox)
    }
}

export default URLForm;