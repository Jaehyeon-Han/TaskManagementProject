package hello.task_management.task.dto.response;

import hello.task_management.task.dto.TaskDto;

public class TaskResponseDtoMapper {
    public static TaskResponseDto fromTaskDto(TaskDto taskDto) {
        TaskResponseDto taskResponseDto = new TaskResponseDto();

        taskResponseDto.setId(taskDto.getId());
        taskResponseDto.setTask(taskDto.getTask());
        taskResponseDto.setAuthor(taskDto.getAuthor());
        taskResponseDto.setCreatedAt(taskDto.getCreatedAt());
        taskResponseDto.setLastModifiedAt(taskDto.getLastModifiedAt());

        return taskResponseDto;
    }
}
