import {
    BrowserRouter as Router,
    Route, Switch
  } from "react-router-dom";

import Home from "./pages/home/Home"
import ExtLander from "./pages/extLander/ExtLander"

function BaseRouter() {
  return (
    <div className="BaseRouter">
      <Router>
        <Switch>
            <Route exact path="/" component={Home}></Route>
            <Route component={ExtLander} />
        </Switch>
       </Router>
    </div>
  );
}

export default BaseRouter;
