import axios from "axios";
import type {Participant} from "@/types/Participant.ts";
import type {User} from "@/types/User.ts";

const eventRoute = "/event";
const authRoute = "/auth";

export function getParticipants(eventId: string): Promise<Participant[]> {
  return axios.get(`${eventRoute}/participants/${eventId}`).then((response) => response.data);
}

export function loadUsers(participantUuids: string[]): Promise<User[]> {
  return axios.post(`${authRoute}/users/`, participantUuids).then((response) => response.data);
}

export function loadUser(participantUuid: string): Promise<User> {
  return axios.get(`${authRoute}/users/${participantUuid}`).then((response) => response.data);
}

export function loadEvent(eventId: string): Promise<any> {
  return axios.get(`${eventRoute}/${eventId}`);
}

export function joinEvent(eventId: string): Promise<any> {
  return axios.get(`${eventRoute}/join/${eventId}`);
}
