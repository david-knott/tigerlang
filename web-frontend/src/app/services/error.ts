export interface ErrorCheckResponse {
    items: ErrorDetails[]
}

export interface ErrorDetails {
    name: string,
    line: number,
    col: number
}