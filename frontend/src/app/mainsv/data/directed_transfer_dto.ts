export interface DirectedTransferDTO {
    senderAccountNumber: string,
    senderName: string,
    receiverAccountNumber: string,
    receiverName: string,
    transferAmount: number,
    title: string,
    timestamp: number,
    incoming: boolean,
}