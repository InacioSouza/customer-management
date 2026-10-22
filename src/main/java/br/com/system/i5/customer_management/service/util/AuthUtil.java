package br.com.system.i5.customer_management.service.util;

import br.com.system.i5.customer_management.model.enuns.UserProfileEnum;

public interface AuthUtil {

    UserProfileEnum getUserProfileInSession();
}
