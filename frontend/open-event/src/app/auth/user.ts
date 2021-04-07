export class User {
  constructor(
    public id: string,
    public externalId: string,
    public userName: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public iconUrl: string,
    public serviceUser: boolean
  ) {
  }
}
