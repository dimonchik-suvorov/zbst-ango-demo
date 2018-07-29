import React from 'react'
import uploadToServer from '../../api/uploadToServer'

class FileUpload extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      //TODO: support upload status in future
      uploadStatus: false,
      selectedFile: null
    }

    this.uploadHandler = this.uploadHandler.bind(this)
    this.fileChangedHandler = this.fileChangedHandler.bind(this)
  }

  fileChangedHandler = file => {
    this.setState({ selectedFile: file[0] })
  }

  uploadHandler = () => {
    const formData = new FormData()
    formData.append(
      'file',
      this.state.selectedFile,
      this.state.selectedFile.name
    )
    uploadToServer(formData)
  }

  render() {
    return (
      <div className={'content'}>
        <input
          type="file"
          onChange={e => this.fileChangedHandler(e.target.files)}
        />
        <button onClick={this.uploadHandler}>Upload!</button>
      </div>
    )
  }
}

export default FileUpload
