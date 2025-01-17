import {reactive} from "vue";
import {io} from "socket.io-client";

export const state = reactive({
  connected: false,
  fooEvents: [],
  barEvents: []
});
const URL = "http://localhost:8080/ws/";

export const socket = io(URL, {withCredentials: true});

socket.on("connect", () => {
  state.connected = true;
});

socket.on("disconnect", () => {
  state.connected = false;
});
