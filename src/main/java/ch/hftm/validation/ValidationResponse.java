package ch.hftm.validation;

import java.util.List;

public record ValidationResponse(long id, boolean valid, List<String> reasons) {}
