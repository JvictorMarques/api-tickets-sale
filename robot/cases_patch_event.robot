*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot

Test Setup    Criar Sessao API

*** Variables ***
${TAG_ALL_TESTS}    smoke    regression
${TAG_SUCCESS}      success
${TAG_FAILURE}      failure
${TAG_DATES}        dates
${TAG_CAPACITY}     capacity
${TAG_AGE}          age
${TAG_CONFLICT}     conflict

*** Test Cases ***

TC-PATCH-EVENT-001 - Atualizar evento com dados válidos
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    
    &{payload}=    Gerar Payload Sucesso Minimo
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH    ${payload}    ${STATUS_201} 
    ${nome_atualizado}=    FakerLibrary.Word  
    ${UPDATED_EVENT_LOCATION}=    FakerLibrary.City
    &{payload_patch}=    Criar Payload Patch Multiplo     name=${nome_atualizado}    location=${UPDATED_EVENT_LOCATION}    capacity=${UPDATED_EVENT_CAPACITY}    ageRestriction=${UPDATED_EVENT_AGE_RESTRICTION}
    ${reponse_patch}     Executar Patch Evento    ${event_id}    ${payload_patch}      ${STATUS_200}
    

TC-PATCH-EVENT-002 - Atualizar múltiplos campos válidos
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    
    &{payload}=    Gerar Payload Sucesso Minimo
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH    ${payload}    ${STATUS_201} 
    ${novo_nome}=    FakerLibrary.Word
    ${nova_descricao}=    FakerLibrary.Sentence
    &{payload_patch}=    Criar Payload Patch Multiplo    name=${novo_nome}    description=${nova_descricao}
    ${response_patch}=    Executar Patch Evento    ${event_id}    ${payload_patch}    ${STATUS_200}
    Log To Console    ${response_patch.json()}

TC-PATCH-EVENT-003 - Tentativa de atualização com ID inválido
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_ERROR}
    
    ${id_invalido}=    Set Variable    ${uuid_inexistente}
    ${novo_nome}=    FakerLibrary.Word
    &{payload_patch}=    Criar Payload Patch    name    ${novo_nome}
    ${response_patch}=    Executar Patch Evento    ${id_invalido}    ${payload_patch}    ${STATUS_404}
    Log To Console    ${response_patch.json()}

TC-PATCH-EVENT-004 - Atualizar apenas o nome do evento
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    
    &{payload}=    Gerar Payload Sucesso Minimo
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH    ${payload}    ${STATUS_201} 
    ${novo_nome}=    FakerLibrary.Word    
    &{payload_patch}=    Criar Payload Patch    name    ${novo_nome}
    ${response_patch}=    Executar Patch Evento   ${event_id}    ${payload_patch}      ${STATUS_200}
    Log To Console    ${response_patch.json()}

TC-PATCH-EVENT-005 - Atualizar apenas a localização do evento
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    
    &{payload}=    Gerar Payload Sucesso Minimo
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH    ${payload}    ${STATUS_201} 
    ${nova_location}=    FakerLibrary.City    
    &{payload_patch}=    Criar Payload Patch    location    ${nova_location}
    ${response_patch}=    Executar Patch Evento   ${event_id}    ${payload_patch}      ${STATUS_200}
    Log To Console    ${response_patch.json()}

TC-PATCH-EVENT-006 - Atualizar evento para data inicial anterior à data atual
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}

    &{payload}=    Gerar Payload Sucesso Minimo
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH    ${payload}    ${STATUS_201} 
    ${payload_patch}=    Criar Payload Patch   dateInitial    ${PAST_DATE}
    ${response_patch}=    Executar Patch Evento   ${event_id}    ${payload_patch}      ${STATUS_400}

TC-PATCH-EVENT-007 - Atualizar evento para data final anterior à data atual
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}

    &{payload}=    Gerar Payload Sucesso Minimo
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH    ${payload}    ${STATUS_201} 
    ${payload_patch}=    Criar Payload Patch   dateFinal    ${PAST_DATE}
    ${response_patch}=    Executar Patch Evento   ${event_id}    ${payload_patch}      ${STATUS_400}

TC-PATCH-EVENT-008 - Aumentar faixa etária do evento quando já houver ingressos vendidos
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    ${ticket_name}=   FakerLibrary.Word
    &{payload}=    Gerar Payload Sucesso Completo Aleatório
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH   ${payload}    ${STATUS_201} 
    &{ticket_payload}=  Criar Payload Ticket Basico    ${ticket_name}    ${100}    ${50}
    ${response}=        Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    ${201}
    ${novo_age_restriction}=    Set Variable    20
    ${payload_patch}=    Criar Payload Patch    ageRestriction    ${novo_age_restriction}
    ${response_patch}=    Executar Patch Evento   ${event_id}    ${payload_patch}      ${STATUS_200}

TC-PATCH-EVENT-009 - Diminuir faixa etária do evento quando já houver ingressos vendidos
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    ${ticket_name}=   FakerLibrary.Word
    &{payload}=    Gerar Payload Sucesso Completo Aleatório
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH   ${payload}    ${STATUS_201} 
    &{ticket_payload}=  Criar Payload Ticket Basico    ${ticket_name}    ${100}    ${50}
    ${response}=        Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    ${201}
    ${novo_age_restriction}=    Set Variable    16
    ${payload_patch}=    Criar Payload Patch    ageRestriction    ${novo_age_restriction}
    ${response_patch}=    Executar Patch Evento   ${event_id}    ${payload_patch}      ${STATUS_200}

TC-PATCH-EVENT-010 - Atualizar evento com datas conflitantes
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_CONFLICT}
    
    &{payload}=    Gerar Payload Sucesso Completo Com Massa Repetida
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH   ${payload}    ${STATUS_201} 
    &{payload_2}=    Gerar Payload Sucesso Completo Aleatório
    ${event_id_2}    ${response}=    Executar POST e Validar Status PATCH   ${payload_2}    ${STATUS_201} 
    ${payload_patch}=    Criar Payload Patch Com Datas Conflitantes
    ${response_patch}=    Executar Patch Evento   ${event_id_2}    ${payload_patch}      ${STATUS_409}

TC-PATCH-EVENT-010 - Atualizar evento para capacidade menor que ingressos vendidos
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_CAPACITY}
    
    &{payload}=    Gerar Payload Sucesso Minimo
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH   ${payload}    ${STATUS_201} 
    ${ticket_name_1}=   FakerLibrary.Word
    ${ticket_name_2}=   FakerLibrary.Word
    &{ticket_payload}=  Criar Payload Ticket Basico    ${ticket_name_1}    ${100}    ${50}
    ${response}=        Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    ${201}
    &{ticket_payload_2}=  Criar Payload Ticket Basico    ${ticket_name_2}    ${100}    ${30}
    ${response}=        Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload_2}    ${201}
    ${payload_patch}=    Criar Payload Patch    capacity    ${79}
    ${response_patch}=    Executar Patch Evento   ${event_id}    ${payload_patch}      ${STATUS_409}

TC-PATCH-EVENT-011 - Atualizar evento para capacidade maior que ingressos vendidos
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_CAPACITY}
    
    &{payload}=    Gerar Payload Sucesso Minimo
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH   ${payload}    ${STATUS_201} 
    ${ticket_name_1}=   FakerLibrary.Word
    ${ticket_name_2}=   FakerLibrary.Word
    &{ticket_payload}=  Criar Payload Ticket Basico    ${ticket_name_1}    ${100}    ${50}
    ${response}=        Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    ${201}
    &{ticket_payload_2}=  Criar Payload Ticket Basico    ${ticket_name_2}    ${100}    ${30}
    ${response}=        Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload_2}    ${201}
    ${payload_patch}=    Criar Payload Patch    capacity    ${90}
    ${response_patch}=    Executar Patch Evento   ${event_id}    ${payload_patch}      ${STATUS_200}