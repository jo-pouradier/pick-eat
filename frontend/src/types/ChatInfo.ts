export interface ChatInfo {
  chatId: string;
  type: string;
  eventId: string;
  userId: string;
  content: string;
  imagePath: string | null;
  date: Date;
}
