export interface BillPartDTO {
  id: string | null
  billId: string
  userId: string | null
  price: number
  text: string
}

export interface BillDTO {
  id: string | null
  userId: string
  eventId: string
  bucketName: string | null
  path: string
  total_price: number
  parts: BillPartDTO[] | null | []
}
