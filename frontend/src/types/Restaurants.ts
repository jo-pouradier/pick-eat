export default interface Restaurant {
    id: string
    name: string,
    address: string,
    lat: number,
    lon: number,
    type: string,
    bucket: string,
    picture: string,
    price_level: number,
    generatedPicture: string,
    icons: string[],
    description: string,
}
