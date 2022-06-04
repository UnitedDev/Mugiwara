package fr.kohei.mugiwara.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Replacement {

    private final CharSequence index;
    private final Object replace;

}
