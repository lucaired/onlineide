import {HttpHeaders} from '@angular/common/http';
import {environment} from '@env';

export function getAuthHeaders(): HttpHeaders {
  if (environment.production) {
    return new HttpHeaders();
  }
  const headers = new HttpHeaders();
  // headers.append('X-XSRF-TOKEN', `${environment.xsrfToken}`);
  return headers;
}
