import axios from 'axios'
import currentLocation from './currentLocation'

const url = `${currentLocation}/scheme`

const body = value => ({
  topic: 'temp value',
  logs: JSON.stringify(value)
})

const params = {
  headers: {
    'Content-Type': 'application/json',
    Authorization: ''
  }
}

const getSchema = value => axios.post(url, body(value), params)

export default getSchema
