export interface EventInfo {
    id: string;
    name: string;
    date: Date;
    address: string;
    latitude: number;
    longitude: number;
    radius: number;
    description: string;
    organizerId: string | null;
    voteFinished: boolean;
    types: string[];
    getCoords(): [number, number];
    setCoords(lat: number, long: number): void;
    setCoords(coords: [number, number]): void;
}
