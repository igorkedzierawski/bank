export interface LoginAttemptDTO {
    id: number,
    device: string,
    ip: string,
    timestamp: number,
    type: LoginAttemptDTO_Type,
}

export enum LoginAttemptDTO_Type {
    SUCCESS = "SUCCESS",
    FAILURE = "FAILURE",
}