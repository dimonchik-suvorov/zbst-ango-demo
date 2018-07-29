import React from 'react'
import FileUpload from './FileUpload'


class Upload extends React.Component {
  constructor(params) {
    super(params)
    this.state = {}
  }

  render() {
    return (
      <div>
        <h2> File upload </h2>
        <FileUpload />
      </div>
    );
  }
}

export default Upload


