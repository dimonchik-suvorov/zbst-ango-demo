import axios from 'axios'
import currentLocation from './currentLocation'

const url = `${currentLocation}upload-file`

const params = {
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'X-Custom-Header': 'foobar',
    Authorization: ''
  },
  timeout: 3000
}

const uploadToServer = value => axios.post(url, value, params)

export default uploadToServer
