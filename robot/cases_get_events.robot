*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot

Test Setup    Criar Sessao API

*** Variables ***
${TAG_ALL_TESTS}    smoke    regression
${TAG_SUCCESS}      success
${TAG_FAILURE}      failure
${TAG_GET}          get

*** Test Cases ***

GET-EVENTS-001 - Validar listagem de eventos cadastrados utilizando filtro de nome
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar listagem de eventos cadastrados
    
    ${event_name}=      FakerLibrary.Word
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             description=Descricao completa do evento.
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             ageRestriction=18
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_201}
    Executar Get Event    name    ${event_name}    ${STATUS_200}

GET-EVENTS-002 - Validar listagem de eventos cadastrados utilizando filtro de localização
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar listagem de eventos cadastrados
    
    ${event_name}=      FakerLibrary.Word
    ${event_location}=  FakerLibrary.City
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             description=Descricao completa do evento.
    ...             location=${event_location}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             ageRestriction=18
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_201}
    Executar Get Event    location    ${event_location}    ${STATUS_200}

GET-EVENTS-003 - Validar listagem de eventos cadastrados utilizando filtro de data
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar listagem de eventos cadastrados
    
    ${event_name}=      FakerLibrary.Word
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             description=Descricao completa do evento.
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             ageRestriction=18
    ...             dateInitial=${FUTURE_DATE_INITIAL_GET}
    ...             dateFinal=${FUTURE_DATE_FINAL_GET}
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_201}
    Executar Get Event Date    dateInitial    ${FUTURE_DATE_INITIAL}    dateFinal    ${FUTURE_DATE_FINAL}    ${STATUS_200}

GET-EVENTS-004 - Validar listagem de eventos cadastrados utilizando filtro de faixa etária
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar listagem de eventos cadastrados
    
    ${event_name}=      FakerLibrary.Word
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             description=Descricao completa do evento.
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             ageRestriction=18
    ...             dateInitial=${FUTURE_DATE_INITIAL_GET}
    ...             dateFinal=${FUTURE_DATE_FINAL_GET}
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_201}
    Executar Get Event    ageRestriction    18    ${STATUS_200}

GET-EVENTS-005 - Validar listagem de eventos cadastrados utilizando filtro de disponibilidade true
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar listagem de eventos cadastrados
    
    Executar Get Event    available    true    ${STATUS_200}

GET-EVENTS-006 - Validar listagem de eventos cadastrados utilizando filtro de disponibilidade false
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar listagem de eventos cadastrados
    
    Executar Get Event    available    false    ${STATUS_200}