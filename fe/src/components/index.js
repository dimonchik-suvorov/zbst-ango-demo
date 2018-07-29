import React from 'react'
import { Route, Link } from 'react-router-dom'
import Home from './home/index'
import Search from './customSearch'
import Upload from './upload'
import SweetHome from './sweetHome'

const App = () => (
  <div>
    <header>
      <Link to="/">Intro</Link>{' | '}
      <Link to="/home">Home</Link>{' | '}
      <Link to="/search">Search</Link>{' | '}
      <Link to="/uploadfile">Search</Link>
    </header>

    <main>
      <Route exact path="" component={SweetHome} />
      <Route exact path="/home" component={Home} />
      <Route exact path="/search" component={Search} />
      <Route exact path="/uploadfile" component={Upload} />
    </main>
  </div>
)

export default App
