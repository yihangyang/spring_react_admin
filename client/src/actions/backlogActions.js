import axios from 'axios'
import { DELETE_PROJECT, GET_BACKLOG, GET_ERRORS, GET_PROJECT, GET_PROJECTS } from './types'

export const addProjectTask = (backlog_id, project_task, history) => async dispatch => {
  try {
    await axios.post(`/api/backlog/${backlog_id}`, project_task)
    history.push(`/projectBoard/${backlog_id}`)
    dispatch({
      type: GET_ERRORS,
      payload: {}
    })
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    })
  }
}

export const getBacklog = (backlog_id, history) => async dispatch => {
  try {
    const res = await axios.get(`/api/backlog/${backlog_id}`)
    // history.push(`/projectBoard/${backlog_id}`)
    dispatch({
      type: GET_BACKLOG,
      payload: res.data
    })
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    })
  }
}