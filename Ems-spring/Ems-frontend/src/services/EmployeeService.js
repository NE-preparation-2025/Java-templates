import axios from 'axios'

const REST_API_BASE_URL= 'http://localhost:8012/api/employees';

export const listEmployees =()=> axios.get(REST_API_BASE_URL);
export const createEmployee =(employee) => axios.post(REST_API_BASE_URL, employee)
export const getEmployee = (employeeId) => axios.get(REST_API_BASE_URL + '/' + employeeId)
export const updateEmployee =(employeeId, employee) => axios.put(REST_API_BASE_URL + '/'+ employeeId, employee)