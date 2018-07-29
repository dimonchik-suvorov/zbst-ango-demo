import React from 'react'
import FileUpload from '../upload/FileUpload'
import Search from '../search'

class SweetHome extends React.Component {
  constructor() {
    super()
    this.state = {
    }
  }

  render() {

    return (
      <React.Fragment>
        <h2>Upload File</h2>
        <FileUpload/>
        <hr />

        <h2>Make Search</h2>
        <Search/>

      </React.Fragment>
    )
  }
}

export default SweetHome
