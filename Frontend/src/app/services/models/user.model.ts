export enum Role {
  GUEST_USER = 'GUEST_USER',
  REGISTERED_USER = 'REGISTERED_USER',
}

export interface UserResponse {
  id: number;
  firstName: string;
  lastName: string;
  role: Role;
  userDetails?: {
    id: number;
    email: string;
    telephone?: string;
  };
}

export interface UserResponse{
  id: number;
  firstName: string;
  lastName: string;
  role: Role;
  userDetails?: {
    id: number;
    email: string;
    telephone?: string;
  };
}

