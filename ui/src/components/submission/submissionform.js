import React, { Component } from 'react';

class URLForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            submissionSuccess: null,
            submitError: '',
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

    // Send inputs to API
    handleSubmit(event) {
        if(this.state.URLShort === '') {
            this.setState({
                submitError: 'Must have URL to shorten'
            }); 
            return
        } 
        if(this.state.URLPref !== '' && !this.state.URLPref.match("[A-Za-z0-9]")) {
            this.setState({
                submitError: 'Preferred URL must not contain symbols or spaces'
            }); 
            return
        } 
        console.log("passed validation")
        const requestOptions = {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ URLShort: this.state.URLShort,
                                   URLPref: this.state.URLPref})
        };
        fetch('http://localhost:8080/api/Shorten/', requestOptions)
            .then(response => response.json())
            .then(result => {
                this.setState({
                  submissionSuccess: result.success, 
                  submittedURL: result.hostedURL})
              })
    }

    handleReset(event) {
        // Reset state for new shorten
        this.setState({
            submissionSuccess: null,
            submitError: '',
            submittedURL: '',
            URLShort: '',
            URLPref: ''});
    }

    render(){
        const submissionSuccess = this.state.submissionSuccess;
        const submittedURL = this.state.submittedURL ? this.state.submittedURL : 'random String';
        const submissionerror = this.state.submitError
        let submitBox;
        if(!submissionSuccess){
            submitBox = submissionerror ?
                <div>
                    {submissionerror}
                    <button onClick={this.handleReset}>
                    Shorten another URL!
                    </button>
                </div>
                :
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