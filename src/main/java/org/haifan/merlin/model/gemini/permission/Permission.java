package org.haifan.merlin.model.gemini.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/tuning/permissions#resource:-permission">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private String name;
    private GranteeType granteeType;
    private String emailAddress;
    private Role role;
}
