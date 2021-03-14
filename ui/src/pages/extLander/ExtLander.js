import React, { Component } from 'react';
import logo from './logo.svg';
import './extLander.css';

class ExtLander extends Component {
    constructor(props) {
        super(props);
        this.state = {
          Response: false,
          URLDirect: '',
          promiseError: false
        };
    }

    componentDidMount() {
      this.getURL();
    }

    //retrieve redirect from API
    getURL = () => {
      let URLKey = (window.location.href.split('/')[3])
      fetch("http://localhost:8080/api/Redirect/" + URLKey)
            .then(response => response.json())
            .then(result => {
              this.setState({
                Response: true, 
                URLDirect: result.link})
            }).catch(error => {
              this.setState({
                  promiseError: true
              })
            })
    }

    render() {
      const promiseError = this.state.promiseError
      const pageLink = this.state.URLDirect;
      const validURl = this.state.Response;
      let redirect
      if(promiseError){ //if fetch promise fails
          redirect = 
          <p>
            Something went wrong!
          </p>
      }else if(pageLink) { 
        //sets URL in browser if found
        window.location.href = pageLink
        redirect = 
          <p>
            Enjoy !!
          </p>
      }else{
        redirect = validURl ? 
          //if request fails
          <p>
            URL Not found !!
          </p>
        :  //if still waiting
          <p>  
            Fetching URL now...
          </p>
      }

      return (
      //encapsulates spinning logo (Still react default)
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