import {KeycloakService} from "keycloak-angular";

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      try {
        await keycloak.init({
          config: {
            url: 'https://auth.psm.church/auth',
            realm: 'open-church',
            clientId: 'open-church-frontend'
          },
          initOptions: {
            onLoad: 'login-required',
            checkLoginIframe: false
          },
          enableBearerInterceptor: true,
          loadUserProfileAtStartUp: true,
          bearerExcludedUrls: ['/assets', '/img']
        });
        resolve(0);
      } catch (error) {
        reject(error);
      }
    });
  };
}
