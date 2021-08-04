package uk.gov.ons.ctp.integration.rhcucumber;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GlueConst {
  public static final String COMMON_TAGS = "not @ignore";
  public static final boolean STRICTNESS = true;
}
