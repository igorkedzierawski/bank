import { Option } from "src/app/option/option.module";

export interface LoginResponse {
    isSuccessful: Option<boolean>,
    message: string,
}