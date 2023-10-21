package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.exception.DataException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    @Autowired
    private final LabelRepository labelRepository;

    @Override
    public Label createNewLabel(LabelDto labelDto) {
        Label label = new Label();
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(long id, LabelDto labelDto) {
        Label labelToUpdate = labelRepository.findById(id);
        labelToUpdate.setName(labelDto.getName());
        return labelRepository.save(labelToUpdate);
    }

    @Override
    public void deletelabel(Long id) throws DataException {
        Label label = labelRepository.findById(id).orElseThrow();
        List<Task> listOfTasks = label.getTasks();
        if (listOfTasks == null || listOfTasks.isEmpty()) {
            labelRepository.deleteById(id);
        } else {
            throw new DataException("The label is associated with the task! Cannot be deleted! ");
        }
    }

    @Override
    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    @Override
    public Label getLabelById(Long id) {
        return  labelRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Label> getAllLabelById(List<Long> ids) {
        return labelRepository.findAllById(ids);
    }
}
