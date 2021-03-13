import React, { Component } from 'react';
import { Link } from "react-router-dom"
import logo from './logo.svg';
import './extLander.css';

class ExtLander extends Component {
    constructor(props) {
        super(props);
        this.state = {
          URLValid: true,
          URLDirect: ''
        };
    }

    componentDidMount() {
      this.getURL();
    }

    //separate handlers to keep track of both boxes
    getURL = () => {
      let URLKey = (window.location.href.split('/')[3])
      fetch("http://localhost:8080/api/Redirect/" + URLKey)
            .then(response => response.json())
            .then(result => {
              this.setState({
                URLValid: result.valid, 
                URLDirect: result.link})
            })
    }

    render() {
      const pageLink = this.state.URLDirect;
      const validURl = this.state.URLValid;
      let redirect
      if(pageLink) {
        redirect = <Link to={{ pathname: {redirect} }} />;
      }else{
        redirect = validURl ?
          <div className="App">
            <header className="App-header">
              <img src={logo} className="App-logo" alt="logo" />
                <p>
                  Fetching URL now...
                </p>
              </header>
          </div>
        :
          <div>
            <header className="App-header">
              <img src={logo} className="App-logo" alt="logo" />
                <p>
                  URL Not found !!
                </p>
              </header>
          </div>
      }

      return (redirect);
    }
}

export default ExtLander;