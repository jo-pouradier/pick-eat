import axios from "axios";

const eventRoute = "/event";
const authRoute = "/auth";

export function getParticipants(eventId: string): Promise<any> {
  return axios.get(`${eventRoute}/participants/${eventId}`);
}

export function loadParticipants(participantUuids: string[]): Promise<any> {
  return axios.post(`${authRoute}/users/`, participantUuids);
}

export function loadEvent(eventId: string): Promise<any> {
  return axios.get(`${eventRoute}/${eventId}`);
}

export function joinEvent(eventId: string): Promise<any> {
  return axios.post(`${eventRoute}/join/${eventId}`);
}
