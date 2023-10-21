package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.exception.DataException;
import hexlet.code.model.Label;

import java.util.List;

public interface LabelService {
    Label createNewLabel(LabelDto labelDto);

    Label updateLabel(long id, LabelDto labelDto);

    void deletelabel(Long id) throws DataException;

    List<Label> getAll();

    Label getLabelById(Long id);

    List<Label> getAllLabelById(List<Long> id);
}
