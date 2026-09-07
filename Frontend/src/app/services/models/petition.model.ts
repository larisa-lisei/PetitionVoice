export enum PetitionState {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  COMPLETED = 'COMPLETED'
}

export interface PetitionResponse {
  id: number;
  title: string;
  category: string;
  description: string;
  state: PetitionState;
  goal: number;
  currentSignatures: number;
  creationDate: Date;
  expirationDate: Date;
  creatorName: string;
  feedback?: string;
  imageUrl?: string | null;
}

export interface PetitionCreateRequest {
  title: string;
  category: string;
  description: string;
  goal: number;
  expirationDate: Date | string;
}

export interface PetitionUpdateRequest {
  title?: string;
  description?: string;
  goal?: number;
  expirationDate?: Date | string;
}

export interface PetitionFeedbackRequest {
  state: PetitionState;
  feedback: string;
}

export interface PetitionSignRequest{
  firstName: string;
  lastName: string;
  email: string;
  telephone: string;
  idPetition: number;
}