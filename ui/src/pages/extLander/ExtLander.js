import React, { Component } from 'react';
import logo from './logo.svg';
import './extLander.css';

class ExtLander extends Component {
    constructor(props) {
        super(props);
        this.state = {
          Response: false,
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
                Response: true, 
                URLDirect: result.link})
            })
    }

    render() {
      const pageLink = this.state.URLDirect;
      const validURl = this.state.Response;
      let redirect
      if(pageLink) {
        window.location.href = pageLink
        redirect = 
          <p>
            Enjoy !!
          </p>
      }else{
        redirect = validURl ?
          <p>
            URL Not found !!
          </p>
        :
          <p>
            Fetching URL now...
          </p>
      }

      return (
      <div>
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
            {redirect}
          </header>
      </div>
      );
    }
}

export default ExtLander;