import React, { Component } from 'react';

class URLForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            response: false,
            clientError: '',
            submittedURL: null,
            URLShort: '',
            URLPref: '',
            promiseError: false};

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
        //clientside validation
        if(!this.state.URLShort) {
            this.setState({
                clientError: 'Must have URL to shorten'
            }); 
            return
        } 
        if(this.state.URLPref !== '' && !this.state.URLPref.match("[A-Za-z0-9]*")) {
            this.setState({
                clientError: 'Preferred URL must not contain symbols or spaces'
            }); 
            return
        } 
        if(this.state.URLPref.length > 40) {
            this.setState({
                clientError: 'Preferred URL must not be over 40 characters'
            }); 
            return
        }
        if(this.state.URLShort.length > 150) {
            this.setState({
                clientError: 'Original URL must not be over 150 characters'
            }); 
            return
        }

        //send request
        const requestOptions = {
            method: 'PUT',
            headers: { 'content-type': 'application/json'},
            body: JSON.stringify({ redirectURL: this.state.URLShort,
                                   prefURL: this.state.URLPref})
        };
        fetch('http://localhost:8080/api/Shorten/', requestOptions)
            .then(response => response.json())
            .then(result => {
                this.setState({
                  response: true, 
                  submittedURL: result.hostedURL})
              }).catch(error => {
                  this.setState({
                      promiseError: true
                  })
              })
    }

    handleReset(event) {
        // Reset state for new shorten
        this.setState({
            response: false,
            clientError: '',
            submittedURL: null,
            URLShort: '',
            URLPref: '',
            promiseError: false});
    }

    render(){
        const location = window.location.href
        const promiseError = this.state.promiseError //if promise failed
        const response = this.state.response; //true or false 
        const submittedURL = this.state.submittedURL //Contains host URL
        const clientError = response && submittedURL === '' ? "This host URL is taken" : this.state.clientError //Contains client error if exists
        let submitBox;
        if(!response){
            submitBox = clientError ? //if clientside validation fails input
                <div>
                    {clientError}
                    <button onClick={this.handleReset}>
                    Shorten another URL!
                    </button>
                </div>
                :
                <div>
                    This tool will create a short URL {location}[Custom host] to redirect to the original URL.
                    <br/>
                    <br/>
                    Please include 'http(s)://' before any web address.
                    <div>
                        <label>
                            Original URL:
                            <input type="text" value={this.state.URLShort} onChange={this.handleURLChange} />
                        </label>
                    </div>
                    <div>
                        <label>
                            (Optional) Preferred host:
                            <input type="text" value={this.state.URLPref} onChange={this.handlePrefChange} />
                        </label>
                    </div>
                    <button onClick={this.handleSubmit}>Submit</button>
                </div>
        }else{
            if(promiseError){
                submitBox = 
                    <div>
                        Something went wrong!
                        <button onClick={this.handleReset}>
                            Try again
                        </button>
                    </div>
            }else{
                submitBox = clientError ? //if serverside validation fails input
                    <div>
                        {clientError}
                        <button onClick={this.handleReset}>
                            Shorten another URL!
                        </button>
                    </div>
                :
                    <div> 
                        <div>
                            URL successfully shortened to <a href={submittedURL}>{location}{submittedURL}</a>
                        </div> 
                        <button onClick={this.handleReset}>
                            Shorten another URL!
                        </button>
                    </div>
            }
        }

        return(submitBox)
    }
}

export default URLForm;