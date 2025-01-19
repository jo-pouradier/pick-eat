import type {EventVote} from "@/types/EventVote.ts";

export interface Participant {
    uuid: string;
    eventId: string;
    userId: string;
    isOrganizer: boolean;
    hasVoted: boolean;
    votes: EventVote[];
}
