package sparta.enby.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sparta.enby.model.Account;
import sparta.enby.model.UserRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";

    private final Account account;

    public UserDetailsImpl(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole userRole = account.getRole();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + userRole.toString());
        Collection<GrantedAuthority>authorities = new ArrayList<>();
        authorities.add(authority);

        return authorities;
    }

    @Override
    public String getUsername() {
        return account.getNickname();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
