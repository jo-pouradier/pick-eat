export interface ChatInfo {
  chatId: string;
  type: string;
  eventId: string;
  userId: string;
  content: string;
  imagePath: string | null;
  date: Date; // 2025-01-17T14:00:56.348+00:00 format
}
