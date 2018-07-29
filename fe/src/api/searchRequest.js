import axios from 'axios'
import currentLocation from './currentLocation'

const url = value => `${currentLocation}search?pattern=${value}`

const searchRequest = value => axios.get(url(value))

export default searchRequest
