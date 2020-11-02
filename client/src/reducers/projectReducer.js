import { DELETE_PROJECT, GET_PROJECT, GET_PROJECTS } from '../actions/types'

const initalState =  {
  projects: [],
  project: {}
};

export default function(state=initalState, action) {
  switch(action.type) {
    case GET_PROJECTS:
      return {
        ...state,
        projects: action.payload
      }
    case GET_PROJECT:
      return {
        ...state,
        project: action.payload
      }
    case DELETE_PROJECT:
      return {
        ...state,
        // project: action.payload
        projects: state.projects.filter(project=>project.projectIdentifier !== action.payload)
      }
    default:
      return state
  }
}