import type {User} from "@/types/User.ts";

export function getUserCookie(): User | undefined {
  const userCookie = document.cookie.split('; ').find(row => row.startsWith('user='));
  if (userCookie) {
    const userTmp = JSON.parse(decodeURIComponent(userCookie.split('=')[1]));
    return  {...userTmp, uuid: userTmp.uuid}
  }else{
    return undefined;
  }
}
